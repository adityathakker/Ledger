mkdir -p bin
javac -cp src -d bin src/ledger/LedgerImpl.java
javac -cp src -d bin src/LedgerServer.java
javac -cp src -d bin src/LedgerClient.java