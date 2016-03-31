<!-- toc -->
# Sublime
### 模板
    ### 
    + ****
    ```
    
    ```

### 1、vi模式
+ **Preferences-->Settings-User**
```json
    "ignored_packages":
    [
        "Markdown",
        "Vintage"          //如果要关闭vi模式，注释这一行
    ]
```

### 2、保存去除行尾空格
+ **MultiMarkdown-->User Setting**
```json
    "trim_trailing_white_space_on_save": true
```

### 3、`ii`退出vi模式
+ **Preferences-->Key Bindings-User**
```json
    { "keys": ["i","i"], "command": "exit_insert_mode",
        "context":
        [
            { "key": "setting.command_mode", "operand": false },
            { "key": "setting.is_widget", "operand": false }
        ]
    },
    { "keys": ["i","i"], "command": "hide_auto_complete", "context":
        [
            { "key": "auto_complete_visible", "operator": "equal", "operand": true }
        ]
    },
    { "keys": ["i","i"], "command": "vi_cancel_current_action", "context":
        [
            { "key": "setting.command_mode" },
            { "key": "selection_empty", "operator": "equal", "operand": true, "match_all": false },
            { "key": "vi_has_input_state" }
        ]
    }
```

### 跳转 [mysql](mysql56.md)