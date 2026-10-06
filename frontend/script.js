const API_URL = "http://localhost:8080";


function addTransaction()
{
    const sender =
        document.getElementById("sender").value;

    const receiver =
        document.getElementById("receiver").value;

    const amount =
        document.getElementById("amount").value;

    const type =
        document.getElementById("type").value;


    const transaction =
    {
        senderAccount: sender,

        receiverAccount: receiver,

        amount: Number(amount),

        transactionType: type,

        timestamp: new Date().toISOString()
    };


    fetch(API_URL + "/transactions/add",
    {
        method: "POST",

        headers:
        {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(transaction)
    })


    .then(function(response)
    {
        if (!response.ok)
        {
            throw new Error(
                "Failed to add transaction."
            );
        }

        return response.json();
    })


    .then(function()
    {
        document.getElementById("message").textContent =
            "Transaction added successfully.";

        document.getElementById("message").style.color =
            "green";

        document.getElementById("transactionForm").reset();

        loadTransactions();
    })


    .catch(function(error)
    {
        document.getElementById("message").textContent =
            error.message;

        document.getElementById("message").style.color =
            "red";
    });
}


function loadTransactions()
{
    fetch(API_URL + "/transactions/all")

    .then(function(response)
    {
        return response.json();
    })


    .then(function(transactions)
    {
        const table =
            document.getElementById("transactionTable");


        table.innerHTML = "";


        for (let i = 0; i < transactions.length; i++)
        {
            const row =
                document.createElement("tr");


            row.innerHTML =
                "<td>" +
                transactions[i].id +
                "</td>" +

                "<td>" +
                transactions[i].senderAccount +
                "</td>" +

                "<td>" +
                transactions[i].receiverAccount +
                "</td>" +

                "<td>" +
                transactions[i].amount +
                "</td>" +

                "<td>" +
                transactions[i].fraudStatus +
                "</td>";


            table.appendChild(row);
        }
    })


    .catch(function()
    {
        console.log(
            "Could not load transactions."
        );
    });
}


document.getElementById("transactionForm")
    .addEventListener(
        "submit",
        function(event)
        {
            event.preventDefault();

            addTransaction();
        }
    );


loadTransactions();
