function addTeacher(url) {
    const data = {
        username: document.getElementById("username").value,
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value
    };
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
            .then(response => response.json())
            .then(result => {
                if (result.message) {
                    showSuccessModal(result.message);
//                    closeAndUpdateTeacherList(modalId);
                    reloadPageOnModalClose("successModal");
                } else {
                    showErrorModal("Đã có lỗi xảy ra. Vui lòng thử lại.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorModal("Đã có lỗi xảy ra. Vui lòng thử lại.");
            });
}

function showSuccessModal(message) {
    document.querySelector('#successMessageContent').textContent = message;
    var successModal = new bootstrap.Modal(document.getElementById('successModal'));
    successModal.show();
}

function showErrorModal(message) {
    document.querySelector('#errorMessageContent').textContent = message;
    var errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
    errorModal.show();
}

function reloadPageOnModalClose(modalId) {
    const modalElement = document.getElementById(modalId);
    modalElement.addEventListener('hidden.bs.modal', function () {
        location.reload();
    }, {once: true});
}

function updateResetPasswordTime(url) {
    const confirmed = confirm("Bạn có chắc chắn muốn đặt lại mật khẩu không?");
    
    if (confirmed) {
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})  
        })
        .then(response => {
            if (response.ok) {
                alert('Đặt lại mật khẩu thành công!');
                window.location.reload(); 
            } else {
                alert('Đã xảy ra lỗi khi đặt lại mật khẩu');
            }
        })
        .catch(error => {
            alert('Lỗi kết nối với máy chủ');
            console.error(error);
        });
    } else {
        alert('Hành động đã bị hủy!');
    }
}

