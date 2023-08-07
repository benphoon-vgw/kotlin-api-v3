CREATE TYPE transaction_type AS ENUM ('CREDIT', 'DEBIT');

CREATE TABLE transaction_ledger
(
    position            BIGINT              GENERATED ALWAYS AS IDENTITY,
    wallet_id           UUID                NOT NULL,
    wallet_version      INTEGER             NOT NULL,
    transaction_id      TEXT                NOT NULL,
    type                transaction_type    NOT NULL,
    coins               INTEGER             NOT NULL,
    closing_balance     BIGINT              NOT NULL,

    PRIMARY KEY (wallet_id, wallet_version)
);

ALTER TABLE transaction_ledger
    ADD CONSTRAINT wallet_transaction_combo UNIQUE (wallet_id, transaction_id)