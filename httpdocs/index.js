var userId = 0;

function doLogin()
{

	userId = 0;
    var $login = $('#UserName');
    var $password = $('#Password');
    
    var jsonPayload = {
        login: $login.val(),
        password: $password.val(),
    }

    var payloadString = JSON.stringify(jsonPayload);

	$.ajax({
        type: 'POST',
        url: 'http://knightfinder.com/WEBAPI/Login.aspx',
        data: payloadString,

        success: function(data) {
            userId = data.EmployeeID;
           

            if(userId < 1)
            {
                alert("UserName/Password combination is incorrect")
                return;
            }
            else{
                localStorage.setItem("EmployeeID", userId);
                localStorage.setItem("StoreID", data.StoreID);
                localStorage.setItem("JobType", data.JobType);
                localStorage.setItem("FirstName", data.FirstName);
                localStorage.setItem("LastName", data.LastName);
                localStorage.setItem("Email", data.Email);
                localStorage.setItem("Phone", data.Phone);
                localStorage.setItem("Status", data.Status);
                location.href = "homepage.html";
            }
        },

        error: function(error) {
            alert("Error contacting api: " + error);
        }
        
    
    });
    return false;
}