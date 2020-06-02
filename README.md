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
* **Final Value**: `String`
* **Default Take**: `true` (start the cooldown)
* **Variable**
  * `advanced_cooldown`
  * `advanced_cooldown_s` or `advanced_cooldown_seconds`
  * `advanced_cooldown_m` or `advanced_cooldown_minutes`
  * `advanced_cooldown_h` or `advanced_cooldown_hours`
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
