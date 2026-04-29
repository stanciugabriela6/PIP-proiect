#!/bin/bash
cd "$(dirname "$0")"
echo "Pornire API Face Recognition pe http://127.0.0.1:8000 ..."
python -m uvicorn app:app --host 127.0.0.1 --port 8000 --reload