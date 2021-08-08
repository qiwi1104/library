function setUnderline() {
	if (document.URL.includes("bookstoread/english")) {
		document.getElementById("bookstoread-english").className = "underlineSelected";
	} else if (document.URL.includes("bookstoread/russian")) {
		document.getElementById("bookstoread-russian").className = "underlineSelected";
	} else if (document.URL.includes("bookstoread/spanish")) {
		document.getElementById("bookstoread-spanish").className = "underlineSelected";
	} else if (document.URL.includes("finishedbooks/english")) {
		document.getElementById("finishedbooks-english").className = "underlineSelected";
	} else if (document.URL.includes("finishedbooks/russian")) {
		document.getElementById("finishedbooks-russian").className = "underlineSelected";
	} else if (document.URL.includes("finishedbooks/spanish")) {
		document.getElementById("finishedbooks-spanish").className = "underlineSelected";
	}
}

document.onload = setTimeout(setUnderline, 100);