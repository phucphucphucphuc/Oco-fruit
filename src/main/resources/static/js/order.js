document.addEventListener('DOMContentLoaded', function() {

    const checkboxes = document.querySelectorAll('input[name="fruitIds"]');
    const limitNoti = document.getElementById('limitNoti');
    const limitNoti2 = document.getElementById('limitNoti2');
    const addCartBtn = document.getElementById('addCartBtn');
    const purchaseBtn = document.getElementById('purchaseBtn');
    const MAX = 3;

    limitNoti2.textContent = `Choose ${MAX}!`;
    limitNoti.textContent = `0/${MAX} fruits selected`;

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {

            const checkedCount = document.querySelectorAll('input[name="fruitIds"]:checked').length;

            if (checkedCount > MAX) {
                this.checked = false;
                limitNoti2.textContent = `You can only choose maximum ${MAX} fruits!`;
                limitNoti2.style.cssText = 'background-color: red; padding: 5px 10px; border-radius: 5px; color: white;';
            } else {
                limitNoti2.textContent = checkedCount > 0 ? `Choose ${MAX}!` : '';
                limitNoti2.style.cssText = '';
                limitNoti.textContent = `${checkedCount}/${MAX} fruits selected`;
            }

            // Add to Cart → chỉ cần >= 1
            addCartBtn.disabled = checkedCount < 1;

            // Purchase → vẫn cần đúng 3
            purchaseBtn.disabled = checkedCount !== MAX;
        });
    });
});