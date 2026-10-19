    let tasks = [];

    function saveTasks(){
        localStorage.setItem("tasks",JSON.stringify(tasks));
    }
    function renderTask(task,index){
        const li = document.createElement("li");
        const taskSpan = document.createElement("span");
        taskSpan.textContent = task.text;

        if(task.done){
            taskSpan.style.textDecoration = "line-through";
        }

        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "删除";

        taskSpan.addEventListener("click", function () {
            task.done = !task.done;
            saveTasks();
            renderTasks();
        });

        deleteBtn.addEventListener("click", function () {
            tasks.splice(index, 1);
            saveTasks();
            renderTasks();
        });

        li.appendChild(taskSpan);
        li.appendChild(deleteBtn);

        taskList.appendChild(li);
    }
    function renderTasks(){
        taskList.innerHTML = "";

        tasks.forEach(function(task,index){
            renderTask(task,index);
        });
    }
        
    
    addTaskBtn.addEventListener("click", function () {
            const taskText = taskInput.value.trim();
            if (taskText === "") {
                return;
            }

            const task = {
                text: taskText,
                done: false
            };

            tasks.push(task);
            saveTasks();
            renderTasks();

            taskInput.value = "";
        });
        loadTasks();
        renderTasks();
           

