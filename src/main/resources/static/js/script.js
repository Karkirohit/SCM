console.log("script loading");

//page theme
let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded",()=>{
    changeTheme();
});
function changeTheme() {
    changePageTheme(currentTheme,currentTheme);
    const changeThemeButton = document.querySelector("#theme_change_button");
    changeThemeButton.addEventListener('click', (event) => {
        console.log("change button theme clicked");
        const oldTheme = currentTheme;

        console.log(oldTheme);
        if (currentTheme === "dark") {
            currentTheme = "light";
        } else {
            currentTheme = "dark";
        }
        changePageTheme(currentTheme, oldTheme);

    });


}

// set theme to localstorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}


//get theme from localstorge
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";

}

function changePageTheme(theme, oldTheme) {

    setTheme(currentTheme);
    document.querySelector('html').classList.remove(oldTheme);
    document.querySelector('html').classList.add(theme);
    document.querySelector("#theme_change_button").querySelector("span").textContent =
        currentTheme == "light" ? "Dark" : "Light";

}

//change page theme over