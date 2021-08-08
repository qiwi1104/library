function setVisibility(what, bool) {
        for (let i = 0; i < what.length ; i++) {
            what[i].hidden = bool;
        }
    }

    document.getElementById('stat').onclick = function() {
        show();

        if(document.getElementById('stat').checked) {
            localStorage.setItem('stat', "true");
        } else {
            localStorage.setItem('stat', "false");
        }
    }

    if (localStorage.getItem('stat') == "true") {
        document.getElementById("stat").setAttribute('checked','checked');

        let hidden = document.getElementsByClassName('hidden');
        setVisibility(hidden, false);
    }

    function show() { // показывает/скрывает доп. информацию
        let hidden = document.getElementsByClassName('hidden');
        setVisibility(hidden, !hidden[0].hidden);
    }