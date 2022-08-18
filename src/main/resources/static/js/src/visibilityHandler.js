function setVisibility(what, bool) {
        for (let i = 0; i < what.length ; i++) {
            what[i].hidden = bool;
        }
    }

    document.getElementById('info').onclick = function() {
        show();

        if(document.getElementById('info').checked) {
            localStorage.setItem('info', "true");
        } else {
            localStorage.setItem('info', "false");
        }
    }

    if (localStorage.getItem('info') == "true") {
        document.getElementById("info").setAttribute('checked','checked');

        let hidden = document.getElementsByClassName('hidden');
        setVisibility(hidden, false);
    }

    function show() { // показывает/скрывает доп. информацию
        let hidden = document.getElementsByClassName('hidden');
        setVisibility(hidden, !hidden[0].hidden);
    }