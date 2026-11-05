const API_BASE =
    location.port === "5500"
        ? "http://127.0.0.1:8081"
        : "";


// ========================
// 检查当前是否已经登录
// ========================

async function checkAlreadyLoggedIn() {

    try {

        const response = await fetch(
            `${API_BASE}/api/users/me`,
            {
                credentials: "include"
            }
        );

        if (response.ok) {

            // 已经登录了，不需要再看登录页
            window.location.href = "./index.html";

        }

    } catch (error) {

        console.log("当前无法检查登录状态");

    }
}


// ========================
// 登录
// ========================

const loginForm =
    document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            const username =
                document.getElementById("username")
                    .value
                    .trim();

            const password =
                document.getElementById("password")
                    .value;

            const message =
                document.getElementById("message");

            message.textContent = "";

            const response = await fetch(
                `${API_BASE}/api/users/login`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify({
                        username,
                        password
                    })
                }
            );

            if (!response.ok) {

                const data =
                    await response.json()
                        .catch(() => null);

                message.textContent =
                    data?.message || "登录失败";

                return;
            }

            window.location.href = "./index.html";

        }
    );

}


// ========================
// 注册
// ========================

const registerForm =
    document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            const username =
                document.getElementById("username")
                    .value
                    .trim();

            const password =
                document.getElementById("password")
                    .value;

            const message =
                document.getElementById("message");

            message.textContent = "";

            const response = await fetch(
                `${API_BASE}/api/users/register`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        username,
                        password
                    })
                }
            );

            if (!response.ok) {

                const data =
                    await response.json()
                        .catch(() => null);

                message.textContent =
                    data?.message || "注册失败";

                return;
            }

            message.classList.add("success");

            message.textContent =
                "注册成功，正在跳转到登录页...";

            setTimeout(
                () => {
                    window.location.href =
                        "./login.html";
                },
                800
            );

        }
    );

}


// 登录页 / 注册页打开时执行
checkAlreadyLoggedIn();