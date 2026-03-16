document.addEventListener("DOMContentLoaded", () => {
  // Load nav
  fetch("ui/nav.html")
    .then(response => response.text())
    .then(html => {
      document.getElementById("nav-placeholder").innerHTML = html;
    })
    .catch(err => console.error("Cannot load nav", err));

  // Load footer
  fetch("ui/footer.html")
    .then(response => response.text())
    .then(html => {
      document.getElementById("footer-placeholder").innerHTML = html;
    })
    .catch(err => console.error("Cannot load footer", err));
});