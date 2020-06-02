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
* **Variable**
  * `advanced_cooldown`
  * `advanced_cooldown_s` or `advanced_cooldown_seconds`
  * `advanced_cooldown_m` or `advanced_cooldown_minutes`
  * `advanced_cooldown_h` or `advanced_cooldown_hours`
  * `advanced_cooldown_format_<time-format>`
    * Example: `advanced_cooldown_format_HH:mm:ss`
* **Examples**
```yaml
advanced-cooldown: "simple-cooldown" # from config.yml

advanced-cooldown: "cooldown-10000" # from config.yml
```
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
      - "tell: &cYou need to wait {left_test_advanced_cooldown_s} seconds"
    right:
      test:
        advanced-cooldown: "cooldown-10000"
      fail-command:
      - "tell: &cYou need to wait {right_test_advanced_cooldown_s} seconds"
    middle:
      test:
        advanced-cooldown: "cooldown-999"
      fail-command:
      - "tell: &cYou need to wait {middle_test_advanced_cooldown_s} seconds"
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

