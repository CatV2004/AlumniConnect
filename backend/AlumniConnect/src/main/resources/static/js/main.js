function approveAlumni(url) {
    const confirmed = confirm('Bạn có chắc chắn muốn duyệt tài khoản này?');
    if (!confirmed) return;

//    const url = `${window.location.origin}/AlumniConnect/admin/alumnis/approve/${id}`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json' 
        }
    })
    .then(response => {
        if (response.ok) {
            alert('✅ Tài khoản đã được duyệt!');
            location.reload();
        } else if (response.status === 404) {
            alert('❌ Không tìm thấy tài khoản!');
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Có lỗi xảy ra, vui lòng thử lại!');
            });
        }
    })
    .catch(error => {
        console.error('Approve error:', error);
        alert('⚠️ Lỗi khi duyệt tài khoản:\n' + error.message);
    });
}
