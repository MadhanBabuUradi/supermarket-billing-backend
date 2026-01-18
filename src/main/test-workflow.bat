@echo off
echo ================================================
echo TESTING STOCK RESTORATION ON BILL DELETION
echo ================================================
echo.

echo [1] Current product status:
curl -X GET http://localhost:8080/api/products

echo.
echo [2] Creating a new bill...
curl -X POST http://localhost:8080/api/bills ^
  -H "Content-Type: application/json" ^
  -d "{\"customerName\":\"Stock Test\",\"customerPhone\":\"1234567890\",\"paymentMethod\":\"CASH\",\"items\":[{\"productId\":1,\"quantity\":5},{\"productId\":2,\"quantity\":3}]}"

echo.
echo [3] Check products after bill creation (should have less stock):
curl -X GET http://localhost:8080/api/products

echo.
echo [4] Get the created bill ID:
curl -X GET http://localhost:8080/api/bills

echo.
echo [5] Delete the bill (should restore stock)...
curl -X DELETE http://localhost:8080/api/bills/1

echo.
echo [6] Verify bill is deleted:
curl -X GET http://localhost:8080/api/bills

echo.
echo [7] Check products after bill deletion (should have original stock):
curl -X GET http://localhost:8080/api/products

echo.
echo ================================================
echo If stock is restored correctly, the test PASSES!
echo ================================================
pause