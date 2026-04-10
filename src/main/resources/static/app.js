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
            this.display = this.display.substring(0,start)+value+this.display.substring(end);
            this.$nextTick(() => {
                input.selectionStart = input.selectionEnd = start + value.length;
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