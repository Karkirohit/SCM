
console.log("model");

const baseUrl="http://localhost:8081";

const viewContactModel = document.getElementById('view_contact_model');


const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_model',
    override: true
};


const contactModel = new Modal(viewContactModel, options, instanceOptions);

function openModal() {
    contactModel.show();
}

function closeModal() {
    contactModel.hide();
}

async function loadContactData(id) {
    console.log(id);

    try {
        let data = await (await fetch(`${baseUrl}/api/contacts/${id}`)).json();

        console.log(data);

        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.email;
        document.querySelector("#contact_picture").src = (data.picture == null ? 'https://tse3.mm.bing.net/th?id=OIP.Cl56H6WgxJ8npVqyhefTdQAAAA&pid=Api&P=0&h=180' : data.picture);

        document.querySelector("#contact_location").innerHTML = data.address;
        document.querySelector("#contact_description").innerHTML = data.description;

        if (data.favorite) {
            document.querySelector("#contact_favorite").innerHTML = "<i class='fa-solid fa-star' style='color: #FFD43B;'></i>";

        } else {
            document.querySelector("#contact_favorite").innerHTML = "<i class='fa-regular fa-star' style='color: #FFD43B;'></i>";
        }
        document.querySelector("#contact_webSite").innerHTML = data.webSitelink;
        document.querySelector("#contact_webSite").href = data.webSitelink;

        document.querySelector("#contact_linkedInLink").innerHTML = data.linkedinLink;
        document.querySelector("#contact_linkedInLink").href = data.linkedinLink;




        openModal();
    } catch (error) {
        console.log(error);
    }

}

async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to save the changes?",
        icon: "warning",
        color:"blue",
        showCancelButton: true,
        confirmButtonText: "Delete",
        confirmButtonColor:"blue",
        cancelButtonColor:"green"

    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
           const url=`${baseUrl}/user/contacts/delete/`+id;
           window.location.replace(url);
        } 
    });
}