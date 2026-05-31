const imageList = [
    "/assets/about/pack/p1.png",
    "/assets/about/pack/p2.png",
    "/assets/about/pack/p3.png",
    "/assets/about/pack/p4.png"
];

let currentIndex = 0;
let rotationInterval = null;
const imgElement = document.getElementById("rotatingImage");
const ROTATION_SPEED_SECONDS = 2.2;

function updateImageDisplay(index) {
    if (!imgElement) return;
    imgElement.classList.add("img-transition");
    imgElement.src = imageList[index];
    setTimeout(() => {
        imgElement.classList.remove("img-transition");
    }, 300);
}

function nextImage() {
    currentIndex = (currentIndex + 1) % imageList.length;
    updateImageDisplay(currentIndex);
}

function startAutoRotation() {
    if (rotationInterval) clearInterval(rotationInterval);
    rotationInterval = setInterval(nextImage, ROTATION_SPEED_SECONDS * 1000);
}

startAutoRotation();

window.addEventListener("beforeunload", () => {
    if (rotationInterval) clearInterval(rotationInterval);
});