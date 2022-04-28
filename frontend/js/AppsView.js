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
