<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250413140430 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL COMMENT '(DC2Type:datetime_immutable)', available_at DATETIME NOT NULL COMMENT '(DC2Type:datetime_immutable)', delivered_at DATETIME DEFAULT NULL COMMENT '(DC2Type:datetime_immutable)', INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            DROP INDEX email ON user
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE user CHANGE id id BIGINT NOT NULL, CHANGE first_name first_name VARCHAR(255) NOT NULL, CHANGE last_name last_name VARCHAR(255) NOT NULL, CHANGE email email VARCHAR(255) NOT NULL, CHANGE password_hash password_hash VARCHAR(255) NOT NULL, CHANGE phone_number phone_number VARCHAR(20) NOT NULL, CHANGE role role VARCHAR(50) NOT NULL, CHANGE subscription_end_date subscription_end_date DATE NOT NULL, CHANGE is_active is_active TINYINT(1) NOT NULL, CHANGE created_at created_at DATETIME NOT NULL, CHANGE updated_at updated_at DATETIME NOT NULL, CHANGE violation_count violation_count INT NOT NULL, CHANGE location location VARCHAR(255) NOT NULL, CHANGE age age INT NOT NULL
        SQL);
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            DROP TABLE messenger_messages
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE user CHANGE id id BIGINT AUTO_INCREMENT NOT NULL, CHANGE first_name first_name VARCHAR(255) DEFAULT NULL, CHANGE last_name last_name VARCHAR(255) DEFAULT NULL, CHANGE email email VARCHAR(255) DEFAULT NULL, CHANGE password_hash password_hash VARCHAR(255) DEFAULT NULL, CHANGE phone_number phone_number VARCHAR(20) DEFAULT NULL, CHANGE role role VARCHAR(50) DEFAULT NULL, CHANGE subscription_end_date subscription_end_date DATE DEFAULT NULL, CHANGE is_active is_active TINYINT(1) DEFAULT NULL, CHANGE created_at created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, CHANGE updated_at updated_at DATETIME DEFAULT NULL, CHANGE violation_count violation_count INT DEFAULT NULL, CHANGE location location VARCHAR(255) DEFAULT NULL, CHANGE age age INT DEFAULT NULL
        SQL);
        $this->addSql(<<<'SQL'
            CREATE UNIQUE INDEX email ON user (email)
        SQL);
    }
}
