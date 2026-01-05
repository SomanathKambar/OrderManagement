#!/usr/bin/env python3
import argparse
import requests
import json
import sys
import os

BASE_URL = os.getenv("OMS_BASE_URL", "http://localhost:8080")

def check_health():
    """Check the health of the OMS system."""
    try:
        response = requests.get(f"{BASE_URL}/actuator/health")
        if response.status_code == 200:
            print(f"✅ System is Healthy: {response.json().get('status')}")
        else:
            print(f"❌ System Unhealthy: {response.status_code}")
            print(response.text)
    except Exception as e:
        print(f"❌ Connection Error: {e}")

def get_order(order_id):
    """Fetch order details."""
    try:
        response = requests.get(f"{BASE_URL}/api/v1/orders/{order_id}")
        if response.status_code == 200:
            print(json.dumps(response.json(), indent=2))
        else:
            print(f"❌ Order not found or error: {response.status_code}")
    except Exception as e:
        print(f"❌ Error: {e}")

def dry_run_transition(order_id, target_state, reason="CLI validation"):
    """Validate a state transition without persisting."""
    headers = {
        "Idempotency-Key": f"dry-run-{order_id}-{target_state}",
        "X-Dry-Run": "true",
        "Content-Type": "application/json"
    }
    payload = {
        "targetState": target_state,
        "reason": reason
    }
    try:
        response = requests.put(
            f"{BASE_URL}/api/v1/orders/{order_id}/status",
            headers=headers,
            json=payload
        )
        if response.status_code == 200:
            print(f"✅ VALID: Transition to {target_state} is allowed.")
            print(json.dumps(response.json(), indent=2))
        else:
            print(f"❌ INVALID: {response.status_code}")
            print(json.dumps(response.json(), indent=2))
    except Exception as e:
        print(f"❌ Error during dry-run: {e}")

def reset_db():
    """Trigger a database reset (dev profile only)."""
    try:
        response = requests.post(f"{BASE_URL}/api/v1/maintenance/reset-db")
        if response.status_code == 200:
            print(f"✅ SUCCESS: {response.json().get('message')}")
        else:
            print(f"❌ FAILED: {response.status_code}")
            print(response.text)
    except Exception as e:
        print(f"❌ Error during reset: {e}")

def main():
    parser = argparse.ArgumentParser(description="OMS Agent-Friendly CLI")
    subparsers = parser.add_subparsers(dest="command", help="Commands")

    # Health command
    subparsers.add_parser("health", help="Check system health")

    # Reset command
    subparsers.add_parser("reset", help="Reset database to fresh state (Dev only)")

    # Get Order command
    get_parser = subparsers.add_parser("get", help="Get order details")
    get_parser.add_argument("id", type=int, help="Order ID")

    # Dry-run command
    dry_parser = subparsers.add_parser("validate", help="Dry-run a state transition")
    dry_parser.add_argument("id", type=int, help="Order ID")
    dry_parser.add_argument("state", type=str, help="Target state (e.g., PAID, CONFIRMED)")

    args = parser.parse_args()

    if args.command == "health":
        check_health()
    elif args.command == "reset":
        reset_db()
    elif args.command == "get":
        get_order(args.id)
    elif args.command == "validate":
        dry_run_transition(args.id, args.state)
    else:
        parser.print_help()

if __name__ == "__main__":
    main()
