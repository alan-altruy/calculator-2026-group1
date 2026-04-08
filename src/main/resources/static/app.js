const app = Vue.createApp({
    data() {
        return {
            display: '',
            output: null,
            errorMessage: null,
            helpText: ''
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

                this.output = data.result.bigDecimal;
                this.errorMessage = null;

            } catch (error) {
                this.output = "NaN";
                this.errorMessage = "ERROR : Division By Zero !";
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
            await fetch('/api/v1/switchDomain', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ domain: value })
            });
        }
    }
});

app.mount('#app');