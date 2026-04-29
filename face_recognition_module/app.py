from fastapi import FastAPI, Request
from fastapi.responses import PlainTextResponse

app = FastAPI()


@app.get("/health", response_class=PlainTextResponse)
def health():
    return "OK"


@app.post("/register", response_class=PlainTextResponse)
async def register(request: Request):
    email = (await request.body()).decode("utf-8").strip()

    if not email:
        return "REGISTER_FAILED"

    try:
        from run_register import run_register
        success = run_register(email)

        if success:
            return "REGISTER_SUCCESS"
        else:
            return "REGISTER_FAILED"
    except Exception as e:
        return f"REGISTER_FAILED: {e}"


@app.post("/login", response_class=PlainTextResponse)
async def login():
    try:
        from run_login import run_login
        email = run_login()

        if email:
            return email
        else:
            return "LOGIN_FAILED"
    except Exception as e:
        return f"LOGIN_FAILED: {e}"