const app = Vue.createApp({
    data() {
        return {
            display: '',
            output: null,
            errorMessage: null,
            errorTimer: null,
            helpText: '',
            helpVisible: false,
            currentMode: 'RAD',
            currentDomain: 'INTEGER',
            seedValue: 67,
            accuracy: 10
        }
    },
    mounted() {
        fetch('/helper.txt')
            .then(response => response.text())
            .then(text => {
                this.helpText = text;
            });
    },
    methods: {
        isOperationAvailable(operation) {
            const availableOps = {
                'INTEGER': ['+', '-', '*', '/', '^', 'mod', '!', 'abs'],
                'RATIONAL': ['+', '-', '*', '/', '^'],
                'REAL': ['+', '-', '*', '/', '^', 'mod', '!', 'abs', 'ln', 'log', 'sin', 'cos', 'tan', 'sinh', 'cosh', 'tanh', 'arcsin', 'arccos', 'arctan'],
                'COMPLEX': ['+', '-', '*', '/', 'sinh', 'cosh', 'i']
            };
            return availableOps[this.currentDomain]?.includes(operation) ?? false;
        },
        canAddOperation(operation) {
            // Les parenthèses sont toujours disponibles
            if (operation === ')') {
                const openCount = (this.display.match(/\(/g) || []).length;
                const closeCount = (this.display.match(/\)/g) || []).length;
                return openCount > closeCount;
            }
            
            if (operation === '(') {
                return true; // Les parenthèses ouvertes sont toujours disponibles
            }
            
            // Vérifier d'abord si l'opération est disponible dans le domaine
            if (!this.isOperationAvailable(operation)) {
                return false;
            }
            
            // Pour mod, !, ^, et e (10^x), il faut quelque chose avant (un nombre ou une parenthèse fermante)
            if (['mod', '!', '^', 'e'].includes(operation)) {
                if (this.display.length === 0) {
                    return false;
                }
                const lastChar = this.display[this.display.length - 1];
                // Doit finir par un chiffre, une parenthèse fermante, ou une constante
                return /[\d\)e\u03c0\u03c6i]/.test(lastChar);
            }
            
            return true;
        },
        showError(msg) {
            this.errorMessage = msg;
            if (this.errorTimer) clearTimeout(this.errorTimer);
            this.errorTimer = setTimeout(() => {
                this.clearError();
            }, 1500);
            // also allow clearing on any click (one-time)
            if (this._errorClickHandler) document.removeEventListener('click', this._errorClickHandler);
            this._errorClickHandler = () => { this.clearError(); };
            document.addEventListener('click', this._errorClickHandler, { once: true });
        },
        clearError() {
            this.errorMessage = null;
            if (this.errorTimer) {
                clearTimeout(this.errorTimer);
                this.errorTimer = null;
            }
            if (this._errorClickHandler) {
                try { document.removeEventListener('click', this._errorClickHandler); } catch(e){}
                this._errorClickHandler = null;
            }
        },
        toggleHelp() {
            this.helpVisible = !this.helpVisible;
        },
        async evaluate() {
            try {
                const response = await fetch('/api/v1/evaluate', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(this.display)
                });

                const data = await response.json();
                if (!response.ok) {
                    throw new Error(data.result);
                }

                this.output = data.result;
                this.clearError();

            } catch (error) {
                this.output = "NaN";
                this.showError("Invalid input !");
            }
        },

        add(value) {
            const input = this.$refs.InputField;
            const start = input.selectionStart;
            const end = input.selectionEnd;
            
            // Cas spécial pour "i": ajouter "*" avant si y a un chiffre avant
            if (value === 'i' && start > 0) {
                const charBefore = this.display[start - 1];
                // Si le caractère avant est un chiffre ou une parenthèse fermante, ajouter "*"
                if (/[\d\)]/.test(charBefore)) {
                    value = '*i';
                }
            }
            
            this.display = this.display.substring(0,start)+value+this.display.substring(end);
            
            // Cases spéciaux pour positionner le curseur
            let cursorPos = start + value.length; // défaut: fin du texte inséré
            
            if (value === 'mod') {
                // Pour mod: curseur avant "mod" si rien avant, après sinon
                cursorPos = start === 0 ? start : start + value.length;
            } else if (value === '||') {
                // Pour ||: curseur entre les deux barres
                cursorPos = start + 1;
            } else if (value.endsWith('()')) {
                // Pour les fonctions (ln(), log(), sin(), etc): curseur en dedans des parenthèses
                cursorPos = start + value.length - 1;
            }
            
            this.$nextTick(() => {
                input.selectionStart = input.selectionEnd = cursorPos;
                input.focus();
            });
        },
        remove() {
            const input = this.$refs.InputField;
            const start = input.selectionStart;
            const end = input.selectionEnd;
            if(start!==end){
                this.display = this.display.substring(0,start)+this.display.substring(end);
                this.$nextTick(() => {
                    input.selectionStart = input.selectionEnd = start;
                    input.focus();
                });
                return;
            }

            if (start === 0) return;

            this.display = this.display.substring(0, start - 1) + this.display.substring(start);

            this.$nextTick(() => {
                input.selectionStart = input.selectionEnd = start - 1;
                input.focus();
            });
        },
        clear() {
            this.display = '';
            this.output = null;
            this.errorMessage = null;
        },
        async dom(value){
            this.currentDomain=value;
            await fetch('/api/v1/switchDomain', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ domain: value })
            });
        },
        async mode(){
            this.currentMode = this.currentMode === 'RAD' ? 'DEG' : 'RAD';
            await fetch('/api/v1/switchTrigonometric', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ trigono: this.currentMode })
            });
        },
        async setSeed(){
            await fetch('/api/v1/setSeed', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ seed: Number(this.seedValue )})
            });
        },
        async setAccuracy(){
            await fetch('/api/v1/setAccuracy', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ accuracy: Number(this.accuracy )})
            });
        }
    }
});

app.mount('#app');