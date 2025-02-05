$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = moduleURL;
    });

    $("#fileImage").change(function () {
        const fileSize = this.files[0].size;

        if (fileSize > 102400) {
            this.setCustomValidity("You must choose an image less than 100KB!");
            this.reportValidity();
        } else {

            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    const file = fileInput.files[0];
    const reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}