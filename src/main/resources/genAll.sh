#!/bin/bash

PY="python3"
TARGET="gen.py"

BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd "$BASEDIR/coclust" && $PY $TARGET
cd "$BASEDIR/ibknn" && $PY $TARGET
cd "$BASEDIR/mf" && $PY $TARGET
cd "$BASEDIR/nbcf" && $PY $TARGET
cd "$BASEDIR/ubknn" && $PY $TARGET
cd "$BASEDIR/bbcf" && $PY $TARGET
cd "$BASEDIR/bicainet" && $PY $TARGET
cd "$BASEDIR/bcn" && $PY $TARGET