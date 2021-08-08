function actionsHandler(id) {

            let toHide = document.getElementsByClassName("hiddenPanel");
            for (let i = 0; i < toHide.length ; i++) {
                toHide[i].hidden = true;
            }
            toHide[id].hidden = false;
}