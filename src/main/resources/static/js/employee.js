// Giữ nguyên chức năng cũ của bạn, chỉ đảm bảo không lỗi khi load
document.addEventListener('DOMContentLoaded', () => {
  const menuLinks = document.querySelectorAll('.menu-link');
  menuLinks.forEach(link => {
    link.addEventListener('click', () => {
      menuLinks.forEach(l => l.classList.remove('active'));
      link.classList.add('active');
    });
  });

  document.querySelector('.btn-logout')?.addEventListener('click', () => {
    if (confirm('Bạn có chắc chắn muốn đăng xuất?')) alert('Đăng xuất thành công!');
  });

  document.querySelector('.btn-notification')?.addEventListener('click', () => {
    alert('Bạn có 3 thông báo mới!');
  });

  document.querySelectorAll('.action-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const text = btn.textContent.trim();
      alert(`Chức năng "${text}" đang được phát triển!`);
    });
  });
});
