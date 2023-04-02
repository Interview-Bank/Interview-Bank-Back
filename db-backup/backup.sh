#!/bin/bash

set -e

echo "Starting backup..."

# Create a backup file
mysqldump -u root -p"$MYSQL_ROOT_PASSWORD" --host=db.interviewbank interview_bank > /backup/interview_bank-$(date +%Y%m%d%H%M%S).sql

echo "Backup complete!"
