const API_BASE =
    location.port === "5500"
        ? "http://127.0.0.1:8081"
        : "";


let tasks = [];


const priorityInput = document.getElementById("priorityInput");
const deadlineInput = document.getElementById("deadlineInput");
const priorityFilter = document.getElementById("priorityFilter");
const taskList =
    document.getElementById("taskList");

const taskInput =
    document.getElementById("taskInput");

const addTaskBtn =
    document.getElementById("addTaskBtn");

const userStatus =
    document.getElementById("userStatus");

const logoutBtn =
    document.getElementById("logoutBtn");

const totalCount =
    document.getElementById("totalCount");

const pendingCount =
    document.getElementById("pendingCount");

const completedCount =
    document.getElementById("completedCount");


// ========================
// 检查登录状态
// ========================

async function checkLogin() {

    const response = await fetch(
        `${API_BASE}/api/users/me`,
        {
            credentials: "include"
        }
    );

    if (!response.ok) {

        window.location.href =
            "./login.html";

        return false;
    }

    const user =
        await response.json();

    userStatus.textContent =
        user.username;

    return true;
}


// ========================
// 加载任务
// ========================

async function loadTasks() {

    const response = await fetch(
        `${API_BASE}/api/tasks`,
        {
            credentials: "include"
        }
    );

    if (!response.ok) {

        if (response.status === 401) {

            window.location.href =
                "./login.html";

        }

        return;
    }

    tasks =
        await response.json();

    renderTasks();

}


// ========================
// 渲染任务
// ========================

function renderTasks() {

    taskList.innerHTML = "";

    updateStatistics();


    if (tasks.length === 0) {

        taskList.innerHTML = `
            <div class="empty-task">
                目前还没有任务，添加一个吧 👋
            </div>
        `;

        return;
    }


    for (const task of tasks) {

        const taskItem =
            document.createElement("div");

        taskItem.className =
            "task-item";


        // checkbox
        const checkbox =
            document.createElement("input");

        checkbox.type =
            "checkbox";

        checkbox.className =
            "task-check";

        checkbox.checked =
            task.done;


        checkbox.addEventListener(
            "change",
            async function () {

                await updateTask(
                    task.id,
                    {
                        done:
                            checkbox.checked
                    }
                );

            }
        );


        // 任务文字
        const taskText =
            document.createElement("span");

        taskText.className =
            "task-text";

        taskText.textContent =
            task.text;


        if (task.done) {

            taskText.classList.add("done");

        }


        // 操作区域
        const actions =
            document.createElement("div");

        actions.className =
            "task-actions";

        const taskMeta = document.createElement("div");
        taskMeta.className = "task-meta";

        const priorityLabel = document.createElement("span");

        const priorityNames = {
            LOW: "低",
            MEDIUM: "中",
            HIGH: "高"
        };

        priorityLabel.className =
            `priority-badge priority-${task.priority || "MEDIUM"}`;

        priorityLabel.textContent =
            priorityNames[task.priority] || "中";

        taskMeta.appendChild(priorityLabel);

        if (task.deadline) {
            const deadlineLabel = document.createElement("span");

            deadlineLabel.className = "deadline-label";
            deadlineLabel.textContent = `截止 ${task.deadline}`;

            taskMeta.appendChild(deadlineLabel);
        }


        // 编辑
        const editButton =
            document.createElement("button");

        editButton.textContent =
            "编辑";

        editButton.className =
            "edit-button";


        editButton.addEventListener("click", () => {
            const dialog = document.getElementById("editTaskDialog");
            const form = document.getElementById("editTaskForm");
            const textInput = document.getElementById("editTaskText");
            const priorityInput = document.getElementById("editTaskPriority");
            const cancelBtn = document.getElementById("cancelEditBtn");
            const saveBtn = document.getElementById("saveEditBtn");

            // 填入当前任务的数据
            textInput.value = task.text;
            priorityInput.value = task.priority || "MEDIUM";

            // 打开弹窗
            dialog.showModal();

            // 取消
            cancelBtn.onclick = () => dialog.close();

            // 保存
            form.onsubmit = async (event) => {
                event.preventDefault();

                const newText = textInput.value.trim();

                if (!newText) {
                    alert("任务内容不能为空");
                    return;
                }

                saveBtn.disabled = true;

                try {
                    await updateTask(task.id, {
                        text: newText,
                        priority: priorityInput.value
                    });

                    dialog.close();
                } catch (error) {
                    alert(error.message);
                } finally {
                    saveBtn.disabled = false;
                }
            };
        });


        // 删除
        const deleteButton =
            document.createElement("button");

        deleteButton.textContent =
            "删除";

        deleteButton.className =
            "delete-button";


        deleteButton.addEventListener(
            "click",
            async function () {

                await deleteTask(
                    task.id
                );

            }
        );


        actions.appendChild(
            editButton
        );

        actions.appendChild(
            deleteButton
        );


        taskItem.appendChild(checkbox);
        taskItem.appendChild(taskText);
        taskItem.appendChild(taskMeta);
        taskItem.appendChild(actions);
        taskList.appendChild(taskItem);

    }

}


// ========================
// 添加任务
// ========================

async function addTask() {

    const text =
        taskInput.value.trim();


    if (!text) {

        return;

    }


    const response = await fetch(
        `${API_BASE}/api/tasks`,
        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/json"
            },

            credentials:
                "include",

            body: JSON.stringify({
                text,
                priority: priorityInput.value,
                deadline: deadlineInput.value || null
            })
        }
    );


    if (!response.ok) {

        alert("添加任务失败");

        return;
    }


    taskInput.value = "";
    priorityInput.value = "MEDIUM";
    deadlineInput.value = "";



    await loadTasks();

}


// ========================
// 修改任务
// ========================

async function updateTask(
    id,
    changes
) {

    const response = await fetch(
        `${API_BASE}/api/tasks/${id}`,
        {
            method: "PATCH",

            headers: {
                "Content-Type":
                    "application/json"
            },

            credentials:
                "include",

            body:
                JSON.stringify(changes)
        }
    );


    if (!response.ok) {

        alert("修改任务失败");

        return;
    }


    await loadTasks();

}


// ========================
// 删除任务
// ========================

async function deleteTask(id) {

    const response = await fetch(
        `${API_BASE}/api/tasks/${id}`,
        {
            method: "DELETE",

            credentials:
                "include"
        }
    );


    if (!response.ok) {

        alert("删除失败");

        return;
    }


    await loadTasks();

}


// ========================
// 统计
// ========================

function updateStatistics() {

    totalCount.textContent =
        tasks.length;


    const completed =
        tasks.filter(
            task => task.done
        ).length;


    completedCount.textContent =
        completed;


    pendingCount.textContent =
        tasks.length - completed;



}


// ========================
// 退出
// ========================

logoutBtn.addEventListener(
    "click",
    async function () {

        await fetch(
            `${API_BASE}/api/users/logout`,
            {
                method: "POST",

                credentials:
                    "include"
            }
        );


        window.location.href =
            "./login.html";

    }
);


// 添加按钮
addTaskBtn.addEventListener(
    "click",
    addTask
);


// Enter 添加
taskInput.addEventListener(
    "keydown",
    function (event) {

        if (event.key === "Enter") {

            addTask();

        }

    }
);


// ========================
// 页面初始化
// ========================

async function init() {

    const loggedIn =
        await checkLogin();

    if (!loggedIn) {

        return;

    }

    await loadTasks();

}


init();