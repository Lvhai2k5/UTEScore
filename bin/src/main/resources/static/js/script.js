
function setActiveMenu() {
  const page = document.body.dataset.page;
  document.querySelectorAll('.menu-link').forEach(a => {
    const href = a.getAttribute('href') || '';
    if (href.endsWith(page + '.html')) a.classList.add('active');
  });
}
document.addEventListener('DOMContentLoaded', setActiveMenu);

// Simple demo actions
function notify(msg){ alert(msg); }
