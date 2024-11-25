document.addEventListener('DOMContentLoaded', () => {
    const dropdowns = document.querySelectorAll('.login-type, .signup-type');

    dropdowns.forEach((dropdown) => {
        let timer;
        let isShown = false; // 드롭다운 표시 여부

        dropdown.addEventListener('mouseenter', () => {
            clearTimeout(timer); // 기존 타이머 제거
            if (!isShown) { // 처음 보여질 때만 애니메이션 적용
                const ul = dropdown.querySelector('ul');
                ul.style.display = 'block';
                ul.style.opacity = '1';
                ul.style.transform = 'translateY(0)';
                isShown = true;
            }
        });

        dropdown.addEventListener('mouseleave', () => {
            timer = setTimeout(() => {
                const ul = dropdown.querySelector('ul');
                ul.style.display = 'none';
                ul.style.opacity = '0';
                ul.style.transform = 'translateY(-10px)';
                isShown = false;
            }, 300); // 300ms 후 숨김
        });
    });
});