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

    document.getElementById('stat2').onclick = function() {
        showPanels();

        if(document.getElementById('stat2').checked) {
            localStorage.setItem('stat2', "true");
        } else {
            localStorage.setItem('stat2', "false");
        }
    }

    if (localStorage.getItem('stat2') == "true") {
        document.getElementById("stat2").setAttribute('checked','checked');

        let hidden = document.getElementsByClassName('hiddenPanel');
        setVisibility(hidden, false);
    }


    function show() { // показывает/скрывает доп. информацию
        let hidden = document.getElementsByClassName('hidden');
        setVisibility(hidden, !hidden[0].hidden);
    }

    function showPanels() {
    	let hiddenPanel = document.getElementsByClassName('hiddenPanel');
        setVisibility(hiddenPanel, !hiddenPanel[0].hidden);
    }