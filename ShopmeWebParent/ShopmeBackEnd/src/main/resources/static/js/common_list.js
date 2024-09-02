function clearFilter() {
    window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
    const entityId = link.attr("entityId");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure want to delete this " + entityName
        + " ID " + entityId + "?");
    $("#confirmModal").show();

}

document.querySelector('.btn-close').addEventListener('click',
    () => {
        $("#confirmModal").hide();
    });

document.querySelector('[name="test"]').addEventListener('click',
    () => {
        $("#confirmModal").hide();
    });

