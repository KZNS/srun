#!/bin/bash

commit_id=$(curl -s https://api.github.com/repos/kzns/srun/branches/main | grep -o '"sha": *"[^"]*"' | head -n 1 | sed 's/"sha": "//;s/"$//' | cut -c1-7)

echo "#!/usr/bin/env -S scala-cli shebang
//> using repository jitpack
//> using dep com.github.kzns:srun:$commit_id"