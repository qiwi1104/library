function setUnderline() {
	if (document.URL.includes("bookstoread/english")) {
		document.getElementById("bookstoread-english").className = "underlineSelected";
	} else if (document.URL.includes("bookstoread/russian")) {
		document.getElementById("bookstoread-russian").className = "underlineSelected";
	} else if (document.URL.includes("bookstoread/spanish")) {
		document.getElementById("bookstoread-spanish").className = "underlineSelected";
	} else if (document.URL.includes("finishedbook/english")) {
		document.getElementById("finishedbook-english").className = "underlineSelected";
	} else if (document.URL.includes("finishedbook/russian")) {
		document.getElementById("finishedbook-russian").className = "underlineSelected";
	} else if (document.URL.includes("finishedbook/spanish")) {
		document.getElementById("finishedbook-spanish").className = "underlineSelected";
	}
}

document.onload = setTimeout(setUnderline, 100);