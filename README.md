# WarehouseSlot – Bin Slotting & Pick-List Generator (Spring Boot)

Run: `mvn spring-boot:run`  (Java 21, uses H2 in-memory DB by default; MySQL config is in `application.properties`)
Frontend: open http://localhost:8080  (simple HTML page in `src/main/resources/static/index.html`)
H2 console: http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:warehousedb`, user `sa`, empty password)

## Entities (package: model)
Zone (1) - (N) Bin (1) - (N) StockAssignment
Order (1) - (1) PickList (1) - (N) PickItem -> Bin / StockAssignment

## Endpoints (one per feature)
| # | Feature | Method & URL |
|---|---------|--------------|
| 1 | Register zone | POST /api/zones |
| 1 | Register bin | POST /api/bins |
| 2 | Assign incoming stock | POST /api/stock/assign |
| 3 | Create order | POST /api/orders |
| 3 | Generate pick list | POST /api/orders/{id}/picklist |
| 4 | Confirm pick (updates occupancy) | PUT /api/picklists/{id}/pick |
| 5 | Bin utilization report | GET /api/reports/bin-utilization |
| 5 | Item velocity report | GET /api/reports/item-velocity |
| - | Lists | GET /api/zones, /api/bins, /api/stock, /api/orders, /api/picklists/{id} |

## Business rules (enforced in the service layer, before saving)
1. FAST items -> NEAR_DISPATCH zone, SLOW -> FAR zone (falls back to the other zone only if the preferred one has no space).
2. A pick list never contains more than a bin holds (checked when generating AND again when picking).
Violations return HTTP 400 with `{"error": "clear message"}`. Missing records return 404.

## Try it in Postman (in order)
POST /api/zones        {"name":"Zone A","type":"NEAR_DISPATCH","routeOrder":1}
POST /api/zones        {"name":"Zone B","type":"FAR","routeOrder":2}
POST /api/bins         {"code":"A-01","zoneId":1,"capacity":100}
POST /api/bins         {"code":"B-01","zoneId":2,"capacity":200}
POST /api/stock/assign {"itemCode":"SKU-1","velocity":"FAST","quantity":40}   -> goes to A-01
POST /api/stock/assign {"itemCode":"SKU-2","velocity":"SLOW","quantity":50}   -> goes to B-01
POST /api/orders       {"lines":[{"itemCode":"SKU-2","quantity":10},{"itemCode":"SKU-1","quantity":5}]}
POST /api/orders/1/picklist   -> A-01 first, then B-01 (sorted by route)
PUT  /api/picklists/1/pick    -> stock & bin occupancy reduced
GET  /api/reports/bin-utilization
GET  /api/reports/item-velocity

## Edge cases to test
- Order more than in stock              -> 400 "Insufficient stock..."
- Assign more than any bin can hold     -> 400 "No bin has enough free capacity..."
- Negative quantity / blank itemCode    -> 400 validation message
- Generate pick list twice / pick twice -> 400
- Unknown order id                      -> 404
