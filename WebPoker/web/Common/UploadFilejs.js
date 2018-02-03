$(function() { // onload

    $("#uploadForm").submit(function() {

        var file1 = this[0].files[0];
        var formData = new FormData();
        formData.append("fake-key-1", file1);

        $.ajax({

            method: 'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function (e) {
                console.error("Failed to submit");
                $("#result").text("Failed to get result from server " + e);
            },
            success: function (r) {
                var obj=JSON.parse(r);
                if(obj.isSucceed==true) {
                    $("#result").append("<p>Upload Succeeded!!</p>");
                }
                else
                {
                    $("#result").append("<p>"+msgError+"</p>");

                }


            }

        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;


    }