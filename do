#!/usr/bin/env bash

set -eu -o pipefail

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

SITES="pelle.io solidblocks.de"
TEMP_DIR="${DIR}/.temp"

mkdir -p "${TEMP_DIR}"

function clean_temp_dir {
  rm -rf "${TEMP_DIR}"
}

trap clean_temp_dir EXIT


SOLIDBLOCKS_SHELL_VERSION="v0.4.9"
SOLIDBLOCKS_SHELL_CHECKSUM="be51e59e8b46351fc9c7993bbf11f4e660d71f905a3330bafd491a71079bb64f"

# self contained function for initial Solidblocks bootstrapping
function bootstrap_solidblocks() {
  local default_dir="$(cd "$(dirname "$0")" ; pwd -P)"
  local install_dir="${1:-${default_dir}/.solidblocks-shell}"

  local temp_file="$(mktemp)"

  curl -L "${SOLIDBLOCKS_BASE_URL:-https://github.com}/pellepelster/solidblocks/releases/download/${SOLIDBLOCKS_SHELL_VERSION}/blcks-shell-${SOLIDBLOCKS_SHELL_VERSION}.zip" > "${temp_file}"
  echo "${SOLIDBLOCKS_SHELL_CHECKSUM}  ${temp_file}" | sha256sum -c

  mkdir -p "${install_dir}" || true
  (
      cd "${install_dir}"
      unzip -o -j "${temp_file}" -d "${install_dir}"
      rm -f "${temp_file}"
  )
}

function ensure_environment() {

  if [[ ! -d "${DIR}/.solidblocks-shell" ]]; then
    echo "environment is not bootstrapped, please run ./do bootstrap first"
    exit 1
  fi

  source "${DIR}/.solidblocks-shell/log.sh"
  source "${DIR}/.solidblocks-shell/utils.sh"
  source "${DIR}/.solidblocks-shell/pass.sh"
  source "${DIR}/.solidblocks-shell/text.sh"
  source "${DIR}/.solidblocks-shell/software.sh"
  source "${DIR}/.solidblocks-shell/python.sh"

  software_set_export_path
}

function task_bootstrap() {
  bootstrap_solidblocks
  ensure_environment
  software_ensure_hugo "0.123.8" "3e628b6ba89fef2976640af2eb7724babbf7839c0b97d04d2b6958d35027c88d"
  software_ensure_s3cmd
}

function hugo_wrapper {
  local site="${1:-}"
  shift || true
  (
    cd "${DIR}/${site}"
    hugo --themesDir "${DIR}/themes" --destination "${DIR}/output/${site}" "$@"
  )
}

function task_build {
  for site in ${SITES}; do
    hugo_wrapper "${site}"
  done
}

function task_serve {
  local site="${1:-}"
  shift || true

  hugo_wrapper "${site}" "serve" --buildDrafts --verbose --disableFastRender $@
}

function task_usage {
  echo "Usage: $0 build | serve | deploy"
  exit 1
}

function task_deploy {
  s3cmd --host-bucket ${S3_HOST} --host ${S3_HOST} \
    --access_key ${PELLE_IO_ACCESS_KEY} \
    --secret_key ${PELLE_IO_SECRET_KEY} \
      sync --no-mime-magic --guess-mime-type ${DIR}/output/pelle.io/* "s3://pelle.io"
}

ARG=${1:-}
shift || true

case "${ARG}" in
  bootstrap) ;;
  *) ensure_environment ;;
esac

case ${ARG} in
  bootstrap) task_bootstrap "$@" ;;
  build) task_build ;;
  hugo) hugo_wrapper $@ ;;
  serve) task_serve $@ ;;
  deploy) task_deploy ;;
  *) task_usage ;;
esac

