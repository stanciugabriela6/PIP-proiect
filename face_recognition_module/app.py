from fastapi import FastAPI, Request
from fastapi.responses import PlainTextResponse
from run_register import run_register
from run_login import run_login

app = FastAPI()

@app.get("/health", response_class = PlainTextResponse)
def health():
    return "OK"

@app.post("/register", response_class = PlainTextResponse)
async def register(request: Request):
    email = (await request.body()).decode("utf-8").strip()

    if not email:
        return "REGISTER_FAILED"

    success = run_register(email)

    if success:
        return "REGISTER_SUCCESS"
    else:
        return "REGISTER_FAILED"

@app.post("/login", response_class = PlainTextResponse)
def login():
    email = run_login()

    if email:
        return email
    else:
        return "LOGIN_FAILED"