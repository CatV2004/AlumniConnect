document.getElementById('createInvitationForm')?.addEventListener('submit', async function (e) {
    e.preventDefault();

    if (!this.checkValidity()) {
        e.stopPropagation();
        this.classList.add('was-validated');
        return;
    }

    const submitBtn = this.querySelector('button[type="submit"]');
    submitBtn.disabled = true;

    try {
        const groupIds = Array.from($('.select2-groups.select2-original').val() || []).map(Number);
        const userIds = Array.from($('.select2-users.select2-original').val() || []).map(Number);

        const payload = {
            eventName: this.eventName.value,
            eventTime: this.eventTime.value,
            content: this.invitationContent.value,
            lockComment: this.lockComment?.checked || false,
            groupIds,
            userIds
        };

        const response = await fetch('/AlumniConnect/admin/invitations', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Request failed');
        }

        await Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: data.message || 'Lời mời đã được tạo thành công!',
            timer: 1500,
            showConfirmButton: false
        });

        $('#createInvitationModal').modal('hide');

        setTimeout(() => {
            resetForm();
            loadInvitations();
        }, 300);

    } catch (error) {
        console.error("Error:", error);
        let errorMsg = error.message;
        if (error instanceof TypeError) {
            errorMsg = 'Lỗi kết nối mạng';
        }

        await Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: errorMsg,
            timer: 2000
        });
    } finally {
        submitBtn.disabled = false;
    }
});

function resetForm() {
    const form = document.getElementById('createInvitationForm');
    if (!form)
        return;

    form.reset();
    form.classList.remove('was-validated');
    $('.select2-original').val(null);

    $('.preview-images').empty();
}

function viewInvitationDetail(invitationId) {
    fetch(`/AlumniConnect/admin/invitations/${invitationId}`)
            .then(response => response.json())
            .then(data => {
                $('#detailEventName').text(data.eventName);
                $('#detailEventTime').text(new Date(data.eventTime).toLocaleString('vi-VN'));
                $('#detailInvitationContent').text(data.content);

                const statusBadge = $('#detailInvitationStatus');
                statusBadge.removeClass('bg-success bg-secondary');
                statusBadge.addClass(data.post.active ? 'bg-success' : 'bg-secondary');
                statusBadge.text(data.post.active ? 'Hoạt động' : 'Đã đóng');

                // Người tạo
                $('#creatorInfo').html(`
                <img src="${data.post.userId.avatar || '/images/default-avatar.png'}" 
                     class="rounded-circle me-2" width="40" height="40">
                <div>
                    <h6 class="mb-0">${data.post.userId.firstName} ${data.post.userId.lastName}</h6>
                    <small class="text-muted">${data.post.userId.email}</small>
                </div>
            `);

                const groupsList = $('#invitedGroupsList');
                groupsList.empty();
                if (data.ugroupSet && data.ugroupSet.length > 0) {
                    data.ugroupSet.forEach(group => {
                        groupsList.append(`
                        <div class="d-flex align-items-center mb-2">
                            <i class="bi bi-people-fill me-2 text-primary"></i>
                            <span>${group.groupName}</span>
                        </div>
                    `);
                    });
                } else {
                    groupsList.html('<p class="text-muted">Không có nhóm nào được mời</p>');
                }

                const usersList = $('#invitedUsersList');
                usersList.empty();
                if (data.userSet && data.userSet.length > 0) {
                    data.userSet.forEach(user => {
                        usersList.append(`
                        <div class="d-flex align-items-center mb-2">
                            <img src="${user.avatar || '/images/default-avatar.png'}" 
                                 class="rounded-circle me-2" width="30" height="30">
                            <div>
                                <span class="d-block">${user.firstName} ${user.lastName}</span>
                                <small class="text-muted">${user.email}</small>
                            </div>
                        </div>
                    `);
                    });
                } else {
                    usersList.html('<p class="text-muted">Không có thành viên nào được mời</p>');
                }

                const gallery = $('#invitationImagesGallery');
                gallery.empty();
                if (data.post.images && data.post.images.length > 0) {
                    gallery.append('<h6 class="mb-3"><i class="bi bi-images"></i> Hình ảnh</h6>');
                    const row = $('<div class="row g-2"></div>');
                    data.post.images.forEach(image => {
                        row.append(`
                        <div class="col-md-3">
                            <img src="${image.url}" class="img-fluid rounded" alt="Hình ảnh sự kiện">
                        </div>
                    `);
                    });
                    gallery.append(row);
                }

                $('#viewInvitationModal').modal('show');
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire('Lỗi', 'Không thể tải chi tiết lời mời', 'error');
            });
}

function openEditInvitationModal(invitationId) {
    fetch(`/AlumniConnect/admin/invitations/${invitationId}`)
            .then(response => {
                if (!response.ok)
                    throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                if (!data)
                    throw new Error('No data returned');

                $('#editInvitationId').val(data.id);
                $('#editEventName').val(data.eventName);
                $('#editInvitationContent').val(data.content);

                const [year, month, day, hour, minute] = data.eventTime;

                const dateTimeStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
                $('#editEventTime').val(dateTimeStr);


                $('#editInvitationModal').modal('show');

                $('#editInvitationModal').one('shown.bs.modal', function () {
                    if (data.ugroupSet && Array.isArray(data.ugroupSet)) {
                        $('.select2-groups-edit').val(data.ugroupSet.map(g => g.id)).trigger('change');
                    }

                    if (data.userSet && Array.isArray(data.userSet)) {
                        $('.select2-users-edit').val(data.userSet.map(u => u.id)).trigger('change');
                    }
                });
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Không thể tải thông tin lời mời',
                    footer: error.message
                });
            });
}


function updateSelectedItems(selectElement, containerClass, isGroup = false) {
    if (!$(selectElement).hasClass('select2-original')) {
        return;
    }

    const select2Element = $(selectElement).next('.select2-hidden');
    if (!select2Element.length || !select2Element.hasClass('select2-hidden-accessible')) {
        return;
    }

    try {
        const selectedItems = select2Element.select2('data');
        const container = $(selectElement).closest('.card-body').find(containerClass);
        container.empty();

        selectedItems.forEach(item => {
            const badge = $(`
                <div class="badge bg-${isGroup ? 'primary' : 'info'} d-flex align-items-center p-2">
                    <span>${item.text}</span>
                    <button type="button" class="btn-close btn-close-white ms-2" 
                            data-id="${item.id}" aria-label="Xóa"></button>
                </div>
            `);

            badge.find('button').click(function () {
                select2Element.val(select2Element.val().filter(id => id != $(this).data('id'))).trigger('change');
            });

            container.append(badge);
        });
    } catch (error) {
        console.error('Lỗi khi cập nhật selected items:', error);
}
}

function loadInvitations(page = 1, eventName = '', size = 5) {
    const tableBody = $('table tbody');
    tableBody.html(`
        <tr>
            <td colspan="6" class="text-center py-4">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2 mb-0">Đang tải dữ liệu...</p>
            </td>
        </tr>
    `);

    const url = new URL(window.location.origin + '/AlumniConnect/admin/invitations');
    url.searchParams.set('page', page);
    url.searchParams.set('size', size);
    if (eventName) {
        url.searchParams.set('eventName', eventName);
    }

    window.location.href = url.toString();
}

function updateInvitationsTable(invitations) {
    const tableBody = $('table tbody');

    if (!invitations || invitations.length === 0) {
        tableBody.html(`
            <tr>
                <td colspan="6" class="text-center py-4 text-muted">
                    <i class="bi bi-info-circle-fill"></i> 
                    Không tìm thấy lời mời nào phù hợp.
                </td>
            </tr>
        `);
        return;
    }

    let html = '';
    invitations.forEach((invitation, index) => {
        const eventTime = new Date(invitation.eventTime).toLocaleString('vi-VN');

        html += `
            <tr>
                <td>${index + 1}</td>
                <td>
                    <a href="#" class="text-decoration-none" 
                       onclick="viewInvitationDetail('${invitation.id}')"
                       >${invitation.eventName}</a>
                </td>
                <td>${eventTime}</td>
                <td>
                    <span class="badge bg-primary me-1">${invitation.userSet?.length || 0} người</span>
                    <span class="badge bg-info">${invitation.ugroupSet?.length || 0} nhóm</span>
                </td>
                <td>
                    <span class="badge ${invitation.post.active ? 'bg-success' : 'bg-secondary'}">
                        ${invitation.post.active ? 'Hoạt động' : 'Đã đóng'}
                    </span>
                </td>
                <td>
                    <div class="d-flex justify-content-center gap-2">
                        <button class="btn btn-outline-info btn-sm rounded-pill"
                                onclick="viewInvitationDetail('${invitation.id}')">
                            <i class="bi bi-eye"></i> Xem
                        </button>
                        <button class="btn btn-outline-primary btn-sm rounded-pill"
                                onclick="openEditInvitationModal('${invitation.id}')">
                            <i class="bi bi-pencil-square"></i> Sửa
                        </button>
                        ${invitation.post.active ?
                `<button class="btn btn-outline-danger btn-sm rounded-pill"
                                onclick="toggleInvitationStatus('${invitation.id}', false)">
                                <i class="bi bi-x-circle"></i> Vô hiệu
                            </button>` :
                `<button class="btn btn-outline-success btn-sm rounded-pill"
                                onclick="toggleInvitationStatus('${invitation.id}', true)">
                                <i class="bi bi-check-circle"></i> Kích hoạt
                            </button>`}
                        <button class="btn btn-outline-danger btn-sm rounded-pill"
                                onclick="deleteInvitationPermanently('${invitation.id}')">
                            <i class="bi bi-trash"></i> Xóa
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });

    tableBody.html(html);
}

function updatePagination(currentPage, totalPages, searchTerm, pageSize) {
    const pagination = $('.pagination');

    if (totalPages <= 1) {
        pagination.hide();
        return;
    }

    pagination.show();

    const prevItem = pagination.find('li').first();
    prevItem.toggleClass('disabled', currentPage <= 1);
    if (currentPage > 1) {
        prevItem.find('a').attr('href',
                `/AlumniConnect/admin/invitations?page=${currentPage - 1}&size=${pageSize}&eventName=${encodeURIComponent(searchTerm)}`);
    }

    pagination.find('.page-item.disabled .page-link').text(`Trang ${currentPage} / ${totalPages}`);

    const nextItem = pagination.find('li').last();
    nextItem.toggleClass('disabled', currentPage >= totalPages);
    if (currentPage < totalPages) {
        nextItem.find('a').attr('href',
                `/AlumniConnect/admin/invitations?page=${currentPage + 1}&size=${pageSize}&eventName=${encodeURIComponent(searchTerm)}`);
    }
}

function toggleInvitationStatus(invitationId, active) {
    const action = active ? 'kích hoạt' : 'vô hiệu hóa';

    Swal.fire({
        title: 'Xác nhận',
        text: `Bạn có chắc muốn ${action} lời mời này?`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: `Đồng ý ${action}`,
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/AlumniConnect/admin/invitations/${invitationId}/status`, {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({active: active})
            })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            Swal.fire('Thành công', data.message, 'success');
                            loadInvitations(); // Tải lại danh sách
                        } else {
                            throw new Error(data.message);
                        }
                    })
                    .catch(error => {
                        Swal.fire('Lỗi', error.message || 'Có lỗi xảy ra', 'error');
                    });
        }
    });
}

function deleteInvitationPermanently(invitationId) {
    Swal.fire({
        title: 'Xác nhận xóa',
        text: 'Bạn có chắc muốn xóa vĩnh viễn lời mời này?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa vĩnh viễn',
        cancelButtonText: 'Hủy bỏ',
        confirmButtonColor: '#d33'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/AlumniConnect/admin/invitations/${invitationId}/force`, {
                method: 'DELETE'
            })
                    .then(async response => {
                        const message = await response.text(); 
                        if (response.ok) {
                            Swal.fire('Đã xóa', message, 'success');
                            loadInvitations();
                        } else {
                            throw new Error(message);
                        }
                    })
                    .catch(error => {
                        Swal.fire('Lỗi', error.message || 'Có lỗi xảy ra', 'error');
                    });
        }
    });
}


// Xử lý hiển thị các nhóm/người đã chọn
function updateSelectedItems(selectElement, containerClass, isGroup = false) {
    const selectedItems = $(selectElement).select2('data');
    const container = $(selectElement).closest('.card-body').find(containerClass);
    container.empty();

    selectedItems.forEach(item => {
        const badge = $(`
            <div class="badge bg-${isGroup ? 'primary' : 'info'} d-flex align-items-center p-2">
                <span>${item.text}</span>
                <button type="button" class="btn-close btn-close-white ms-2" 
                        data-id="${item.id}" aria-label="Xóa"></button>
            </div>
        `);

        badge.find('button').click(function () {
            const option = $(selectElement).find(`option[value="${$(this).data('id')}"]`);
            option.prop('selected', false);
            $(selectElement).trigger('change');
        });

        container.append(badge);
    });
}

// Xử lý khi thay đổi select nhóm/thành viên
$('.select2-groups').on('change', function () {
    updateSelectedItems(this, '.selected-groups-list', true);
});

$('.select2-users').on('change', function () {
    updateSelectedItems(this, '.selected-users-list');
});

// Xử lý tạo nhóm mới
$('#createGroupForm').submit(function (e) {
    e.preventDefault();

    const groupName = $('#newGroupName').val();
    const description = $('#newGroupDescription').val();
    const members = $('.select2-new-group-members').val();

    // Gọi API tạo nhóm mới
    $.ajax({
        url: '/api/groups',
        method: 'POST',
        data: {
            name: groupName,
            description: description,
            memberIds: members
        },
        success: function (response) {
            // Thêm nhóm mới vào select và chọn nó
            const newOption = new Option(groupName, response.id, true, true);
            $('.select2-groups').append(newOption).trigger('change');

            // Đóng modal và reset form
            $('#createGroupModal').modal('hide');
            $('#createGroupForm')[0].reset();

            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Nhóm mới đã được tạo và tự động chọn!'
            });
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Không thể tạo nhóm mới. Vui lòng thử lại!'
            });
        }
    });
});