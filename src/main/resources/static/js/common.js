window.onload = function onLoad() {
	console.log("ロードの処理には入ってきた");
	
	const backPageCheck = document.getElementById("backPageCheck");
	if (backPageCheck.value == 0) {
		console.log("IF1");
		const pagingBack = document.getElementById("pagingBack");
		pagingBack.style.display = "none";
	}
	const nextPageCheck = document.getElementById("nextPageCheck");
	if (nextPageCheck.value == 0) {
		console.log("IF2");
		const pagingNext = document.getElementById("pagingNext");
		pagingNext.style.display = "none";
	}
}