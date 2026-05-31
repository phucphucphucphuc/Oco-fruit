document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    const addCartBtn = document.getElementById('addCartBtn');
    const purchaseBtn = document.getElementById('purchaseBtn');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const checkedCount = document.querySelectorAll('input[type="checkbox"]:checked').length;
            addCartBtn.disabled = checkedCount === 0;
            purchaseBtn.disabled = checkedCount === 0;
        });
    });
});