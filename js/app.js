
        const taskInput = document.getElementById("taskInput");
        const addTaskBtn = document.getElementById("addTaskBtn");
        const taskList = document.getElementById("taskList");

        addTaskBtn.addEventListener("click", function () {
            const taskText = taskInput.value;
            if (taskText === "") {
                return;
            }
            const li = document.createElement("li");
            const taskSpan = document.createElement("span");
            taskSpan.textContent = taskText;

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "删除";

            taskSpan.addEventListener("click", function () {
                taskSpan.style.textDecoration = "line-through";
            });
            deleteBtn.addEventListener("click", function () {
                li.remove();
            });

            li.appendChild(taskSpan);
            li.appendChild(deleteBtn);

            taskList.appendChild(li);

            taskInput.value = "";
        });
