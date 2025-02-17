# **📌 Bob User Guide**  

<img width="204" alt="Ui" src="https://github.com/user-attachments/assets/b6554ef1-7b92-4b00-bca5-3af74b66b29b" />  

Welcome to **Bob the Chat Bot**! Bob helps you **keep track of tasks, deadlines, and events** effortlessly.  

## **⚡ Features**  

- ✅ Add **To-Do** tasks  
- 📅 Manage **Deadlines**  
- 🎉 Track **Events**  
- 📖 View and manage your task list  
- 💾 **Data persistence** – your tasks are saved automatically!  

---

## **📝 Adding a To-Do Task**  
A To-Do task is a simple task without any specific date/time.  

**Command:**  
```
todo <task description>
```
**Example:**  
```
todo sleep
```

---
## **📆 Adding a Deadline**  
A deadline is a task that must be completed before a specific time.  

**Command:**  
```
deadline <task description> /by <deadline date/time>
```
**Example:**  
```
deadline do homework /by tonight 10pm
```
> ℹ️ for information on supported date formats, see the corresponding [section](#-supported-date-formats) below
---


## **📅 Adding an Event**  
An event is a task that has a specific **start and end** date/time.  

**Command:**  
```
event <event description> /from <event start date/time> /to <event end date/time>
```
**Example:**  
```
event CS2103T meeting /from Friday 3pm /to Friday 4pm
```
---

## **📜 Viewing Your Task List**  
To **see all tasks** in your list, use:  

**Command:**  
```
list
```
---

## **✅ Marking a Task as Done**  
Once you finish a task, mark it as done.  

**Command:**  
```
mark <task number>
```
**Example:**  
```
mark 1
```
---

## **↩️ Unmarking a Task as Done**  
If you accidentally marked a task wrongly, you can unmark the task as done.

**Command:**  
```
unmark <task number>
```
**Example:**  
```
unmark 1
```
---

## **❌ Deleting a Task**  
To remove a task from your list, use:  

**Command:**  
```
delete <task number>
```
**Example:**  
```
delete 2
```

---

## **🔍 Finding Tasks by Keyword**  
Search for tasks by keyword.  

**Command:**  
```
find <keyword>
```
**Example:**  
```
find meeting
```
>ℹ️ the find function returns any corresponding matches, it does not have to be the full word. Even a letter can be used to find corresponding tasks containing that letter.

---

## **📤 Saving & Exiting**  
Bob **automatically saves your tasks** when you exit.  

**Command:**  
```
bye
```
> Alternatively, simply close the window by presing 'X' on the top right corner. 


<br>

## **📌 Additional Features**  

### **📂 Data Persistence**  

Bob **saves your tasks automatically** to `data/bob.txt`. When you reopen Bob, your tasks will be restored.  

---

### **📌 Supported Date Formats**  
You can use various date formats such as:  
- `yyyy-MM-dd` → `2025-02-17`  
- `dd/MM/yyyy HH:mm` → `17/02/2025 22:00`  
- Natural language → `"next Friday"`, `"tomorrow 6pm"`
> ℹ️ if the time is not specified, it will set it as the current time you added the task, on the date you entered. <br>

> ⚠️ some dates, especially in natural language may be inferred wrongly, especially if the date is not a valid day of the month. Bob will still try to infer which date you are referring to, but it can get it wrong. If the date inferred is incorrect, simply delete and add the task again using a different date format.

---
<br>

## **🚀 Getting Started**  

### **📦 Installation**  
1. Download `Bob.jar` from the [Releases](https://github.com/gekjunxu/ip/releases) page.  
2. Simply double click to run and start adding tasks! 🎉  
---




<br>

> ℹ️ _**Note:** This documentation was generated with assistance from ChatGPT for better styling._
