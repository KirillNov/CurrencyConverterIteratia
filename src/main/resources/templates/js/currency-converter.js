function allCurrencies() {
    /**
     * Получаем все символьные обозначения и дату
     */
    return fetch('http://localhost:8081/graphql', {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            query: "{ currencies { charCode, name } date }"
        }),
    })
        .then((res) => {
            return res.json()
        })
        .catch(error => {
            console.log(error);
        });
}

allCurrencies().then(result => {
        addElement("selectFrom", result);
       addElement("selectTo", result);
    }
);
/**
 * Каждый SELECT заполняем валютой
 */
function addElement(id, result) {
    let select = document.getElementById(id);
    let date = document.getElementById("date");
    date.innerText = result.data.date;
    result.data.currencies.forEach(element => {
        let option = document.createElement('option');
        option.innerText = element.charCode + " " + element.name;
        select.append(option);
    });
}

/**
 * Определяем нажатие кнопки конвертации и заполнение результом
 */
function ConvButtonFun() {

    let val1 = document.getElementById("selectFrom").value;
    let val2 = document.getElementById("selectTo").value;
    let sum = document.getElementById("inputFrom").value;
    operateByCharCodes(val1, val2, sum);

    function operateByCharCodes(charCode1, charCode2, sum) {

        let query = `mutation saveOperationsHistory($charCode1: String, $charCode2: String, $sum: String) {
                                saveOperationsHistory(charCode1: $charCode1, charCode2: $charCode2, sum: $sum) {
                                         id,
                                         charCodeFrom,
                                         charCodeTo,
                                         exchangeSum,
                                         resultExchange,
                                         date
                                }
                   }`;

        fetch('http://localhost:8081/graphql', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify({
                query,
                variables: {charCode1, charCode2, sum}
            })
        })
            .then((res) => res.json())
            .then((result) => {
                document.getElementById("inputTo").value = result.data.saveOperationsHistory.resultExchange;
            })
            .catch((error) => {
                console.log(error);
                if (val1 === "NoValue" || val2 === "NoValue") {
                    alert("Выберите валюту");
                }

                if (sum === "") {
                    alert("Укажите сумму для конвертации");
                }
            });
    }
}
/**
 * Определяем нажатие кнопки "История операций" и отображаем таблицу с историей
 */
function HisButtonFun() {
    return fetch('http://localhost:8081/graphql', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
        },
        body: JSON.stringify({
            query: "{ operationsHistory { id, charCodeFrom, charCodeTo, exchangeSum, resultExchange, valueFrom, valueTo, date } }"
        })
    }).then((res) => {
        return res.json()
    }).then(result => {
        document.getElementById("stats").hidden = true;
            document.getElementById("history").hidden = false;

            let list = result.data.operationsHistory;
            let placeholder = document.querySelector("#history_body");
            let out = "";
            for (let operation of list) {
                out += `
         <tr>
            <td>${operation.date}</td>
            <td>${operation.charCodeFrom}</td>
            <td>${operation.valueFrom}</td>
            <td>${operation.charCodeTo}</td>
            <td>${operation.valueTo}</td>
            <td>${operation.exchangeSum}</td>
            <td>${operation.resultExchange}</td>
         </tr>
      `;
            }
            placeholder.innerHTML = out;
        }
    )
}

/**
 * Определяем нажатие кнопки "Статистика" и отображаем таблицу с статистикой по выбранным валютам
 */
function StatButtonFun() {
    let charCode1 = document.getElementById("selectFrom").value;
    let charCode2 = document.getElementById("selectTo").value;


    let query = `query getStats($charCode1: String, $charCode2: String) {
                      stats(charCode1: $charCode1, charCode2: $charCode2) {
                            avgRate,
                            sumValue
                      }
                }`;

    fetch('http://localhost:8081/graphql', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
        },
        body: JSON.stringify({
            query,
            variables: {charCode1, charCode2}
        })
    })
        .then((res) => res.json())
        .then(res => {

            document.getElementById("history").hidden = true;
            document.getElementById("stats").hidden = false;

            let avgRate = res.data.stats.avgRate;
            let sumValue = res.data.stats.sumValue;
            let placeholder = document.querySelector("#stats_body");
            let out = "";
            out += `
         <tr>
            <td>${charCode1}</td>
            <td>${charCode2}</td>
            <td>${avgRate}</td>
            <td>${sumValue}</td>
         </tr>
      `;

            placeholder.innerHTML = out;

        })
        .catch(error => {
            console.log(error);
        });
}




