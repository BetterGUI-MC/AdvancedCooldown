# Advanced Cooldown

## Config.yml

```yaml
# Format: 
# <name>: <value>
# 
# Example:
# simple-cooldown: 100
simple-cooldown: 100
cooldown-10000: 10000
cooldown-999: 999
```

## Requirement Type

* **Keyword**: `advanced-cooldown`
* **Value Type**: `String`
* **Final Value**: `String` (name from `config.yml`)
* **Default Take**: `true` (start the cooldown)
* **Examples**

```yaml
advanced-cooldown: "simple-cooldown" # from config.yml

advanced-cooldown: "cooldown-10000" # from config.yml
```

## Variable

* `advanced_cooldown_<name>`
* `advanced_cooldown_<name>_s` or `advanced_cooldown_<name>_seconds`
* `advanced_cooldown_<name>_m` or `advanced_cooldown_<name>_minutes`
* `advanced_cooldown_<name>_h` or `advanced_cooldown_<name>_hours`
* `advanced_cooldown_<name>_format_<time-format>`
    * Example Format: `advanced_cooldown_<name>_format_HH:mm:ss`

## Example icon

```yaml
advanced-cooldown:
  slot: 12
  id: clock
  name: "&cAdvanced Cooldown"
  command:
    - "tell: &aHello"
  click-requirement:
    left:
      test:
        advanced-cooldown: "simple-cooldown"
      fail-command:
        - "tell: &cYou need to wait {advanced_cooldown_simple-cooldown_s} seconds"
    right:
      test:
        advanced-cooldown: "cooldown-10000"
      fail-command:
        - "tell: &cYou need to wait {advanced_cooldown_cooldown-10000_s} seconds"
    middle:
      test:
        advanced-cooldown: "cooldown-999"
      fail-command:
        - "tell: &cYou need to wait {advanced_cooldown_cooldown-999_s} seconds"
```

## Time Format

| character | duration element |
| --- | --- |
| y | years |
| M | months |
| d | days |
| H | hours |
| m | minutes |
| s | seconds |
| S | milliseconds |

* Example:
    * `HH:mm:ss`: show hours, minutes and seconds
    * `YY:MM:dd HH:mm:ss`: show years, months, days, hours, minutes and seconds

