#!/usr/bin/env sh
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle is not installed or not available in PATH. Install Gradle 8.10+ and retry." >&2
  exit 1
fi
