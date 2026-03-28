const { createApp } = Vue;

createApp({
    data() {
        return {
            expression: "",
            result: null
        };
    },
    methods: {
        compute() {
            fetch("http://localhost:8080/calculate", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    expression: this.expression
                })
            })
                .then(res => res.json())
                .then(data => {
                    this.result = data.result;
                });
        }
    }
}).mount("#app");