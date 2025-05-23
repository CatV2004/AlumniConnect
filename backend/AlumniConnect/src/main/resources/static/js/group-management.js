document.getElementById('createGroupForm')?.addEventListener('submit', function (e) {
    e.preventDefault();

    if (!this.checkValidity()) {
        e.stopPropagation();
        this.classList.add('was-validated');
        return;
    }

    const formData = {
        groupName: document.getElementById('groupName').value,
    };

    fetch('/AlumniConnect/admin/groups', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    })
            .then(response => {
                if (!response.ok)
                    throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Nhóm đã được tạo thành công!',
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => window.location.reload());
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Có lỗi xảy ra khi tạo nhóm: ' + error.message
                });
            });
});

function openEditModal(groupId) {
    fetch(`/AlumniConnect/admin/groups/${groupId}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('editGroupId').value = data.id;
                document.getElementById('editGroupName').value = data.name;
                document.getElementById('editGroupDescription').value = data.description || '';
                new bootstrap.Modal(document.getElementById('editGroupModal')).show();
            });
}

document.getElementById('editGroupForm')?.addEventListener('submit', function (e) {
    e.preventDefault();

    if (!this.checkValidity()) {
        e.stopPropagation();
        this.classList.add('was-validated');
        return;
    }

    const groupId = document.getElementById('editGroupId').value;
    const formData = {
        name: document.getElementById('editGroupName').value,
        description: document.getElementById('editGroupDescription').value
    };

    fetch(`/AlumniConnect/admin/groups/${groupId}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    })
            .then(response => {
                if (!response.ok)
                    throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Thông tin nhóm đã được cập nhật!',
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => window.location.reload());
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Có lỗi xảy ra khi cập nhật nhóm: ' + error.message
                });
            });
});

function viewGroupDetail(groupId) {
    fetch(`/AlumniConnect/admin/groups/${groupId}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('detailGroupName').textContent = data.name;
                document.getElementById('detailGroupDescription').textContent = data.description || 'Không có mô tả';
                document.getElementById('detailCreatedDate').textContent = new Date(data.createdDate).toLocaleDateString('vi-VN');

                const statusBadge = document.getElementById('detailGroupStatus');
                statusBadge.textContent = data.active ? 'Hoạt động' : 'Ngừng hoạt động';
                statusBadge.className = data.active ? 'badge bg-success' : 'badge bg-secondary';

                const memberList = document.getElementById('memberList');
                memberList.innerHTML = '';
                if (data.members?.length > 0) {
                    data.members.forEach(member => {
                        const item = document.createElement('div');
                        item.className = 'd-flex align-items-center mb-2';
                        item.innerHTML = `
                        <div class="flex-shrink-0 me-2">
                            <img src="${member.avatar || '/images/default-avatar.png'}"
                                class="rounded-circle" width="40" height="40" alt="${member.firstName} ${member.lastName}">
                        </div>
                        <div class="flex-grow-1">
                            <h6 class="mb-0">${member.firstName} ${member.lastName}</h6>
                            <small class="text-muted">${member.email}</small>
                        </div>`;
                        memberList.appendChild(item);
                    });
                } else {
                    memberList.innerHTML = '<p class="text-muted">Nhóm chưa có thành viên nào</p>';
                }

                new bootstrap.Modal(document.getElementById('groupDetailModal')).show();
            });
}

function toggleGroupStatus(groupId, activate) {
    const action = activate ? 'activate' : 'deactivate';
    const text = activate ? 'kích hoạt' : 'vô hiệu hóa';

    Swal.fire({
        title: 'Xác nhận',
        text: `Bạn có chắc chắn muốn ${text} nhóm này không?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: `Đồng ý ${text}`,
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/AlumniConnect/admin/groups/${groupId}/${action}`, {method: 'POST'})
                    .then(response => {
                        if (!response.ok)
                            throw new Error('Network response was not ok');
                        return response.json();
                    })
                    .then(data => {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công',
                            text: `Nhóm đã được ${text} thành công!`,
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => window.location.reload());
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: `Có lỗi xảy ra khi ${text} nhóm: ${error.message}`
                        });
                    });
        }
    });
}

function deleteGroupPermanently(groupId) {
    Swal.fire({
        title: 'Xác nhận xóa',
        text: 'Bạn có chắc chắn muốn xóa vĩnh viễn nhóm này không? Hành động này không thể hoàn tác!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa vĩnh viễn',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/AlumniConnect/admin/groups/${groupId}`, {
                method: 'DELETE'
            })
                    .then(response => {
                        if (!response.ok)
                            throw new Error('Không thể xóa nhóm!');
                        Swal.fire({
                            icon: 'success',
                            title: 'Đã xóa',
                            text: 'Nhóm đã được xóa vĩnh viễn.',
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => window.location.reload());
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: error.message
                        });
                    });
        }
    });
}


function openAddMembersModal(groupId) {
    document.getElementById('groupId').value = groupId;

    const select = document.getElementById('userSelect');
    for (let i = 0; i < select.options.length; i++) {
        select.options[i].selected = false;
    }

    const modal = new bootstrap.Modal(document.getElementById('addMembersModal'));
    modal.show();
}

function openAddMembersModal(groupId) {
    const groupIdInput = document.getElementById('groupId');
    const userSelectContainer = document.getElementById('userSelect');
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');

    groupIdInput.value = groupId;
    userSelectContainer.innerHTML = '';
    selectAllCheckbox.checked = false;
    selectAllCheckbox.indeterminate = false;

    fetch(`/AlumniConnect/admin/groups/${groupId}/available-users`)
            .then(response => {
                if (!response.ok)
                    throw new Error('Không thể tải danh sách thành viên');
                return response.json();
            })
            .then(users => {
                if (!users || users.length === 0) {
                    userSelectContainer.innerHTML = '<p class="text-muted">Không có thành viên nào để thêm</p>';
                    return;
                }

                users.forEach(user => {
                    const item = document.createElement('div');
                    item.className = 'd-flex align-items-center mb-2';

                    item.innerHTML = `
                <div class="form-check me-2">
                    <input class="form-check-input user-checkbox" type="checkbox" value="${user.id}" id="userCheck${user.id}" name="userIds" />
                </div>
                <div class="flex-shrink-0 me-2">
                    <img src="${user.avatar || '/images/default-avatar.png'}"
                        class="rounded-circle" width="40" height="40" alt="${user.firstName} ${user.lastName}">
                </div>
                <div class="flex-grow-1">
                    <label class="mb-0" for="userCheck${user.id}">
                        <h6 class="mb-0 d-inline">${user.firstName} ${user.lastName}</h6>
                        <small class="text-muted"> (${user.email})</small>
                    </label>
                </div>
                `;
                    userSelectContainer.appendChild(item);
                });

                const modal = new bootstrap.Modal(document.getElementById('addMembersModal'));
                modal.show();
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: error.message
                });
            });
}

document.getElementById('addMembersForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const groupId = document.getElementById('groupId').value;
    const checkboxes = document.querySelectorAll('#userSelect input[type="checkbox"]:checked');
    const selectedUserIds = Array.from(checkboxes).map(cb => parseInt(cb.value));

    if (selectedUserIds.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: 'Chưa chọn thành viên',
            text: 'Vui lòng chọn ít nhất một thành viên để thêm vào nhóm.'
        });
        return;
    }

    const submitBtn = this.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

    fetch(`/AlumniConnect/admin/groups/${groupId}/members`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(selectedUserIds)
    })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || 'Request failed with status ' + response.status);
                    });
                }
                return Promise.resolve();
            })
            .then(() => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Thành viên đã được thêm vào nhóm!',
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => {
                    const modal = bootstrap.Modal.getInstance(document.getElementById('addMembersModal'));
                    modal.hide();
                    window.location.reload();
                });
            })
            .catch(err => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: err.message || 'Đã xảy ra lỗi khi thêm thành viên.'
                });
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.textContent = 'Thêm thành viên';
            });
});

document.addEventListener('DOMContentLoaded', function () {
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');
    const userSelectDiv = document.getElementById('userSelect');

    if (selectAllCheckbox && userSelectDiv) {
        selectAllCheckbox.addEventListener('change', function () {
            const checkboxes = userSelectDiv.querySelectorAll('.user-checkbox');
            checkboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
        });

        userSelectDiv.addEventListener('change', function (e) {
            if (e.target && e.target.classList.contains('user-checkbox')) {
                const checkboxes = userSelectDiv.querySelectorAll('.user-checkbox');
                const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;

                selectAllCheckbox.checked = checkedCount === checkboxes.length;
                selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < checkboxes.length;
            }
        });
    }
});
