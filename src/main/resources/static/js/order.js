document.addEventListener('DOMContentLoaded', function() {
    
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    const limitNoti = document.getElementById('limitNoti');
    const limitNoti2 = document.getElementById('limitNoti2');
    const addCartBtn = document.getElementById('addCartBtn');

    limitNoti2.textContent = `Choose 3!`;
    limitNoti.textContent = `0/3 fruits selected`;

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            
            const checkedCount = document.querySelectorAll('input[type="checkbox"]:checked').length;

            if (checkedCount > 3) {
                this.checked = false;
                limitNoti2.textContent = `You can only choose maximum 3 fruits!`;
                limitNoti2.style.cssText = 'background-color: red;';
            } else {
                limitNoti.textContent = `${checkedCount}/3 fruits selected`;
            }

            if (checkedCount === 3) {
                addCartBtn.disabled = false;
            } else {
                addCartBtn.disabled = true;
            }

        });
    });

    // Optional: Prevent form submit for demo
    document.getElementById('fruitForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const checked = document.querySelectorAll('input[type="checkbox"]:checked');
        
        if (checked.length === 0) {
            alert("Please select at least one fruit!");
        } else {
            alert(`Added to cart! You selected ${checked.length} fruit(s).`);
            // send
        }
    });
});