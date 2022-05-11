function insertTextCategories() {

    if (document.getElementById("menuApps") !== undefined) {
        let div = document.createElement('div');
        div.setAttribute("id", "categoriesApps");
        div.style.marginTop = "12px";
        div.style.marginBottom = "12px";
        div.innerHTML = "<b>Категории</b>";
        document.getElementById("menuApps").children[0].after(div);
        clearInterval(timerId1);
    }

}
let timerId1 = setInterval(insertTextCategories, 50);




//Прослушивание кликов на сайте
//Используется для:
// 1. Скрытия не нужных ктегорий при переходе по меню с категориями
// 2. Скрытия всплывающего окна при клике по ссылкам в шапке карточек ведущих на сторонний сайт
let timerId2;
document.addEventListener('click',e => {

    let targetClick = e.target;
    //Проверка на клик по ссылке на сторонний сайт в заголовке карточки
    if (targetClick.getAttribute("class") === "anchorApp") {
        timerId2 = setInterval(closeDialogWindow, 50);
    }

    //Проверка на клик по меню с категориями
    if (targetClick.getAttribute("class") !== null &&
        targetClick.getAttribute("class").includes("category-") === true) {
        let sumCategories = targetClick.getAttribute("sum-categories");
        let openCategory = targetClick.getAttribute("class");
        let numOpenCategory = Number(openCategory.replace("category-", ""));
        for (let i = 1; i < sumCategories; i++) {
            if (numOpenCategory === 0) {
                document.getElementById("category-" + i).style.display = "";
            } else {
                if (numOpenCategory !== i) {
                    document.getElementById("category-" + i).style.display = "none";
                }
                if (numOpenCategory === i) {
                    document.getElementById("category-" + i).style.display = "";
                }
            }
        }
    }
    //console.log(targetClick)
});

function closeDialogWindow() {

    let window = document.getElementById("overlay");
    if (window !== null) {
        window.removeAttribute('opened');
        clearInterval(timerId2);
    }

}
