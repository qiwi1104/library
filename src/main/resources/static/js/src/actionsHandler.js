function actionsHandler(id) {
	let toHide = document.getElementsByClassName("hiddenPanel");
	
	for (let i = 0; i < toHide.length ; i++) {
		toHide[i].hidden = true;
	}

	toHide[id].hidden = false;

	let panelElements = document.getElementsByClassName("panel")[0].children;

	for (let i = 0; i < panelElements.length ; i++) {
		panelElements[i].className = "underline";
	}

	document.getElementById(id).className = "underlineSelected";
}